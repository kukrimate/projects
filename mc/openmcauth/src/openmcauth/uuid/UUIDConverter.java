/*
 * Copyright 2016 Kukri Máté
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package openmcauth.uuid;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;

public class UUIDConverter extends TypeAdapter<UUID> {
    public UUIDConverter() {}
    
    @Override
    public void write(JsonWriter writer, UUID uuid) throws IOException {
        writer.value(uuid.toString().replace("-", ""));
    }

    @Override
    public UUID read(JsonReader reader) throws IOException {
        String uuidHexString = reader.nextString();
        return UUID.fromString(uuidHexString.substring(0, 8) + "-" + uuidHexString.substring(8, 12) + "-" + uuidHexString.substring(12, 16) + "-" + uuidHexString.substring(16, 20) + "-" + uuidHexString.substring(20, 32));
    }
    
}
