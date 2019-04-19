package net.minecraftforge.srg2source.ast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraftforge.srg2source.util.Util;
import net.minecraftforge.srg2source.util.io.ConfLogger;
import net.minecraftforge.srg2source.util.io.FolderSupplier;
import net.minecraftforge.srg2source.util.io.InputSupplier;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

@SuppressWarnings("unchecked")
public class RangeExtractor extends ConfLogger<RangeExtractor>
{
    private PrintWriter outFile;
    private final Set<File> libs = new HashSet<File>();
    private InputSupplier src;

    public static boolean extract(File srcDir, File outFile) throws IOException
    {
        RangeExtractor extractor = new RangeExtractor();
        extractor.setSrcRoot(srcDir);

        extractor.addLibs(srcDir);
        
        boolean worked = extractor.generateRangeMap(outFile);

        System.out.println("Srg2source batch mode finished - now exiting " + (worked ? 0 : 1));
        return worked;
    }
    
    public static boolean extract(File srcDir, File libDir, File outFile) throws IOException
    {
        RangeExtractor extractor = new RangeExtractor();
        extractor.setSrcRoot(srcDir);

        extractor.addLibs(libDir);

        boolean worked = extractor.generateRangeMap(outFile);

        System.out.println("Srg2source batch mode finished - now exiting " + (worked ? 0 : 1));
        return worked;
    }
    
    /**
     * Generates the rangemap.
     * @param out The file where the RangeMap will be put out.
     * @return FALSE if it failed for some reason, TRUE otehrwise.
     */
    public boolean generateRangeMap(File out)
    {
        try
        {
            if (!out.exists())
            {
                Files.createParentDirs(out);
                out.createNewFile();
            }

            // setup printwriter
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(out)));
        }
        catch (Exception e)
        {
            // some isue making the output thing.
            Throwables.propagate(e);
            return false;
        }

        log("Symbol range map extraction starting");

        List<String> tmp = src.gatherAll(".java");
        Collections.sort(tmp);
        String[] files = tmp.toArray(new String[tmp.size()]);
        log("Processing " + files.length + " files");

        if (files.length == 0)
        {
            // no files? well.. nothing to do then.
            cleanup();
            return true;
        }

        // convert libs list
        String[] libArray = new String[libs.size()];
        {
            int i = 0;
            for (File f : libs)
                libArray[i++] = f.getAbsolutePath();
        }

        try
        {

            for (String path : files)
            {
                //path = path.replace('\\',  '/');
                //if (!path.equals("net/minecraft/block/BlockHopper.java")) continue;
                InputStream stream = src.getInput(path);

                // do stuff
                {
                    SymbolRangeEmitter emitter = new SymbolRangeEmitter(path, outFile);
                    String data = new String(ByteStreams.toByteArray(stream), Charset.forName("UTF-8")).replaceAll("\r", "");

                    log("processing " + path);

                    CompilationUnit cu = Util.createUnit(path, data, src.getRoot(path), libArray);

                    int[] newCode = getNewCodeRanges(cu, data);

                    PackageDeclaration pkg = cu.getPackage();
                    if (pkg != null)
                    {
                        emitter.emitPackageRange(pkg);
                    }

                    List<ImportDeclaration> imports = cu.imports();
                    for (ImportDeclaration imp : imports)
                    {
                        emitter.emitImportRange(imp);
                    }

                    List<AbstractTypeDeclaration> types = (List<AbstractTypeDeclaration>)cu.types();
                    AbstractTypeDeclaration type = types.get(0);

                    String qualified = ((ITypeBinding)type.getName().resolveBinding()).getQualifiedName();
                    SymbolReferenceWalker walker = new SymbolReferenceWalker(emitter, qualified, newCode);

                    walker.walk(type);

                    emitter.log("endProcessing " + path);
                    emitter.log("");
                }

                stream.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(errorLogger);
        }

        cleanup();
        return true;
    }

    private void cleanup()
    {
        outFile.close();
        outFile = null;
    }

    @Override
    protected void log(String s)
    {
        outFile.println(s);
        outLogger.println(s);
    }

    /*
    private boolean processAbstractClass(SymbolRangeEmitter emitter, AbstractTypeDeclaration type, int[] newCode)
    {
        if (type instanceof AnnotationTypeDeclaration)
        {
            log("Annotation!");
        }
        else if (type instanceof EnumDeclaration)
        {
            log("ENUM!");
        }
        else if (type instanceof TypeDeclaration)
        {
            if (!processClass(emitter, (TypeDeclaration) type, newCode))
            {
                return false;
            }
        }
        return true;
    }

    private boolean processClass(SymbolRangeEmitter emitter, TypeDeclaration clazz, int[] newCode)
    {
        String className = emitter.emitClassRange(clazz);

        if (clazz.getSuperclassType() != null)
        {
            emitter.emitTypeRange(clazz.getSuperclassType());
        }

        for (Type i : (List<Type>) clazz.superInterfaceTypes())
        {
            emitter.emitTypeRange(i);
        }

        // Methods and fields in this class (not 'all', which includes superclass)
        FieldDeclaration[] fields = clazz.getFields();
        for (FieldDeclaration field : fields)
        {
            emitter.emitTypeRange(field.getType());

            for (VariableDeclarationFragment frag : (List<VariableDeclarationFragment>) field.fragments())
            {
                emitter.emitFieldRange(frag);
                // Initializer can refer to other symbols, so walk it, too
                SymbolReferenceWalker walker = new SymbolReferenceWalker(emitter, className, newCode);
                if (!walker.walk(frag.getInitializer()))
                {
                    return false;
                }
            }
        }

        for (MethodDeclaration method : clazz.getMethods())
        {
            if (!processMethod(emitter, className, method, newCode))
            {
                return false;
            }
        }

        // static initializers
        for (BodyDeclaration body : (List<BodyDeclaration>) clazz.bodyDeclarations())
        {
            if (body instanceof Initializer)
            {
                Initializer init = (Initializer) body;
                SymbolReferenceWalker walker = new SymbolReferenceWalker(emitter, className, newCode, "{}", "");
                if (!walker.walk(init.getBody()))
                {
                    return false;
                }
            }
        }

        //Inner classes
        for (BodyDeclaration body : (List<BodyDeclaration>) clazz.bodyDeclarations())
        {
            if (body instanceof AbstractTypeDeclaration)
            {
                if (!processAbstractClass(emitter, (AbstractTypeDeclaration) body, newCode))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean processMethod(SymbolRangeEmitter emitter, String className, MethodDeclaration method, int[] newCode)
    {
        String methodSignature = emitter.emitMethodRange(method);

        // Return type and throws list
        emitter.emitTypeRange(method.getReturnType2());
        for (Name exc : (List<Name>) method.thrownExceptions())
        {
            emitter.emitThrowRange(exc, (ITypeBinding) exc.resolveBinding());
        }

        List<SingleVariableDeclaration> params = (List<SingleVariableDeclaration>) method.parameters();
        HashMap<String, Integer> paramIds = new HashMap<String, Integer>();

        for (int x = 0; x < params.size(); x++)
        {
            SingleVariableDeclaration param = params.get(x);
            emitter.emitTypeRange(param.getType());
            emitter.emitParameterRange(method, methodSignature, param, x);
            paramIds.put(param.getName().getIdentifier(), x);
        }

        // Method body
        SymbolReferenceWalker walker = new SymbolReferenceWalker(emitter, className, newCode, method.getName().getIdentifier(), methodSignature);

        walker.setParams(paramIds);

        return walker.walk(method.getBody());
    }
    */
    private int[] getNewCodeRanges(CompilationUnit cu, String data)
    {
        boolean inside = false;
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (Comment cmt : (List<Comment>) cu.getCommentList())
        {
            String comment = data.substring(cmt.getStartPosition(), cmt.getStartPosition() + cmt.getLength());
            if (cmt.isLineComment())
            {
                String[] words = comment.split(" ");
                if (words.length >= 3)
                {
                    // First word is "//",
                    // Second is "CraftBukkit", "Spigot", "Forge"..,
                    // Third is "start"/"end"
                    //Sometimes they miss spaces, so check if the beginning is smoshed
                    int idx = ((words[0].startsWith("//") && words[0].length() != 2) ? 1 : 2);
                    String command = words[idx];
                    if (command.equalsIgnoreCase("start"))
                    {
                        ret.add(cmt.getStartPosition());
                        if (inside)
                            log("Unmatched newcode start: " + cmt.getStartPosition() + ": " + comment);
                        inside = true;
                    }
                    else if (command.equalsIgnoreCase("end"))
                    {
                        ret.add(cmt.getStartPosition());
                        if (!inside)
                            log("Unmatched newcode end: " + cmt.getStartPosition() + ": " + comment);
                        inside = false;
                    }
                }
            }
            else if (cmt.isBlockComment())
            {
                String[] lines = comment.split("\r?\n");
                for (String line : lines)
                {
                    String[] words = line.trim().split(" ");
                    if (words.length >= 3)
                    {
                        // First word is "/*",
                        // Second is "CraftBukkit", "Spigot", "Forge"..,
                        // Third is "start"/"end"
                        String command = words[2];
                        if (command.equalsIgnoreCase("start"))
                        {
                            ret.add(cmt.getStartPosition());
                            if (inside)
                                log("Unmatched newcode start: " + cmt.getStartPosition() + ": " + comment);
                            inside = true;
                        }
                        else if (command.equalsIgnoreCase("end"))
                        {
                            ret.add(cmt.getStartPosition());
                            if (!inside)
                                log("Unmatched newcode end: " + cmt.getStartPosition() + ": " + comment);
                            inside = false;
                        }
                    }
                }
            }

        }

        int[] r = new int[ret.size()];
        for (int x = 0; x < ret.size(); x++)
        {
            r[x] = ret.get(x);
        }
        return r;
    }

    public InputSupplier getSrc()
    {
        return src;
    }

    public RangeExtractor setSrcRoot(File srcRoot)
    {
        if (srcRoot.isDirectory())
            src = new FolderSupplier(srcRoot);

        return this;
    }

    public RangeExtractor setSrc(InputSupplier supplier)
    {
        src = supplier;
        return this;
    }

    /**
     * @param lib Either a directory or a file
     */
    public RangeExtractor addLibs(File lib)
    {
        if (lib.isDirectory())
            for (File f : lib.listFiles())
                addLibs(f);
        else if (lib.getPath().endsWith("jar")) // to be sure its
            libs.add(lib);

        return this;
    }

    /**
     * @param lib Either a directory or a file
     */
    public RangeExtractor addLibs(String path)
    {
        if (path.contains(File.pathSeparator))
            for (String f : Splitter.on(File.pathSeparatorChar).splitToList(path))
                addLibs(new File(f));
        else
            addLibs(new File(path));

        return this;
    }

    public Set<File> getLibs()
    {
        return libs;
    }
}
