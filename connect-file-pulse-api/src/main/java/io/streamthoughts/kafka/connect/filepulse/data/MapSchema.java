/*
 * Copyright 2019-2020 StreamThoughts.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.kafka.connect.filepulse.data;

import java.util.Map;
import java.util.Objects;

public class MapSchema implements Schema {

    private final Type type;

    private Integer hash;

    private final Schema keySchema = Schema.string();

    private final Schema valueSchema;

    /**
     * Creates a new MapSchema for the specified type.
     *
     * @param valueSchema the {@link Schema} instance.
     */
    MapSchema(final Schema valueSchema) {
        this.type = Type.MAP;
        this.valueSchema = valueSchema;
    }

    public Schema keySchema() {
        return keySchema;
    }

    public Schema valueSchema() {
        return valueSchema;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type type() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T map(SchemaMapper<T> mapper, boolean optional) {
        return mapper.map(this, optional);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T map(final SchemaMapperWithValue<T> mapper, final Object object, final boolean optional) {
        return mapper.map(this, (Map<String, ?>)object, optional);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isResolvable() {
        return valueSchema.isResolvable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema merge(final Schema o) {
        if (this.equals(o)) return this;

        if (o instanceof MapSchema) {
            final MapSchema that = (MapSchema)o;
            return new MapSchema(this.valueSchema().merge(that.valueSchema()));
        }

        throw new DataException("Cannot merge incompatible schema type " + this.type() + "<>" + o.type());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapSchema)) return false;
        MapSchema mapSchema = (MapSchema) o;
        return type == mapSchema.type &&
                Objects.equals(hash, mapSchema.hash) &&
                Objects.equals(keySchema, mapSchema.keySchema) &&
                Objects.equals(valueSchema, mapSchema.valueSchema);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (hash == null) {
            hash = Objects.hash(type, hash, keySchema, valueSchema);
        }
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" +
                "type=" + type +
                ", keySchema=" + keySchema +
                ", valueSchema=" + valueSchema +
                "]";
    }
}