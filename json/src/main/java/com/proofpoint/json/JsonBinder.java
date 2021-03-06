/*
 * Copyright 2010 Proofpoint, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.proofpoint.json;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.MapBinder;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;

public class JsonBinder
{
    private final MapBinder<Class<?>, JsonSerializer<?>> serializerMapBinder;
    private final MapBinder<Class<?>, JsonDeserializer<?>> deserializerMapBinder;

    public static JsonBinder jsonBinder(Binder binder)
    {
        return new JsonBinder(binder);
    }

    private JsonBinder(Binder binder)
    {
        serializerMapBinder = MapBinder.newMapBinder(binder, new TypeLiteral<Class<?>>() { }, new TypeLiteral<JsonSerializer<?>>() {});
        deserializerMapBinder = MapBinder.newMapBinder(binder, new TypeLiteral<Class<?>>() { }, new TypeLiteral<JsonDeserializer<?>>() {});
    }

    public LinkedBindingBuilder<JsonSerializer<?>> addSerializerBinding(Class<?> type)
    {
        Preconditions.checkNotNull(type, "type is null");
        return serializerMapBinder.addBinding(type);
    }

    public LinkedBindingBuilder<JsonDeserializer<?>> addDeserializerBinding(Class<?> type)
    {
        Preconditions.checkNotNull(type, "type is null");
        return deserializerMapBinder.addBinding(type);
    }

    public <T> void bindSerializer(JsonSerializer<T> jsonSerializer)
    {
        Preconditions.checkNotNull(jsonSerializer, "jsonSerializer is null");

        Class<?> type = jsonSerializer.handledType();
        Preconditions.checkNotNull(type, "jsonSerializer.handledType is null");
        Preconditions.checkArgument(type == Object.class, "jsonSerializer.handledType can not be Object.class");
        serializerMapBinder.addBinding(type).toInstance(jsonSerializer);
    }
}
