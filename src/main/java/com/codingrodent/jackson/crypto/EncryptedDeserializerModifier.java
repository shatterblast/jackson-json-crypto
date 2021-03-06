/*
The MIT License

Copyright (c) 2018

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.codingrodent.jackson.crypto;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;

import java.util.Iterator;

/**
 * Class that defines objects that to be used to participate in constructing {@link JsonDeserializer} instances (via {@link DeserializerFactory}).
 */
public class EncryptedDeserializerModifier extends BeanDeserializerModifier {

    private final EncryptionService encryptionService;

    public EncryptedDeserializerModifier(final EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Add deserialization functionality for {@link Encrypt} marked fields
     */
    @Override
    public BeanDeserializerBuilder updateBuilder(final DeserializationConfig config, final BeanDescription beanDescription, final BeanDeserializerBuilder builder) {
        Iterator it = builder.getProperties();

        while (it.hasNext()) {
            SettableBeanProperty p = (SettableBeanProperty) it.next();
            if (null != p.getAnnotation(Encrypt.class)) {
                JsonDeserializer<Object> current = p.getValueDeserializer();
                builder.addOrReplaceProperty(p.withValueDeserializer(new EncryptedJsonDeserializer(encryptionService, current)), true);
            }
        }
        return builder;
    }
}
