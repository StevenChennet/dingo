/*
 * Copyright 2021 DataCanvas
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

package io.dingodb.common.type.scalar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dingodb.common.type.converter.DataConverter;
import io.dingodb.expr.runtime.TypeCode;
import io.dingodb.serial.schema.DingoSchema;
import io.dingodb.serial.schema.DoubleSchema;
import org.apache.avro.Schema;

import java.math.BigDecimal;
import javax.annotation.Nonnull;

@JsonTypeName("double")
public class DoubleType extends AbstractScalarType {
    @JsonCreator
    public DoubleType(@JsonProperty("nullable") boolean nullable) {
        super(TypeCode.DOUBLE, nullable);
    }

    @Override
    public DoubleType copy() {
        return new DoubleType(nullable);
    }

    @Override
    public DingoSchema toDingoSchema(int index) {
        return new DoubleSchema(index);
    }

    @Override
    protected Object convertValueTo(@Nonnull Object value, @Nonnull DataConverter converter) {
        if (value instanceof Float) {
            return BigDecimal.valueOf((Float) value).doubleValue();
        }
        return super.convertValueTo(value, converter);
    }

    @Override
    protected Object convertValueFrom(@Nonnull Object value, @Nonnull DataConverter converter) {
        return converter.convertDoubleFrom(value);
    }

    @Override
    protected Schema.Type getAvroSchemaType() {
        return Schema.Type.DOUBLE;
    }
}
