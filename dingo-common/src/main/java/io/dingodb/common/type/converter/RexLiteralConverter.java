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

package io.dingodb.common.type.converter;

import io.dingodb.common.type.DingoType;
import io.dingodb.expr.runtime.utils.NumberUtils;
import org.apache.calcite.avatica.util.ByteString;
import org.apache.calcite.util.NlsString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.annotation.Nonnull;

public class RexLiteralConverter implements DataConverter {
    public static final RexLiteralConverter INSTANCE = new RexLiteralConverter();
    private static final RuntimeException NEVER_CONVERT_BACK
        = new IllegalStateException("Convert back to RexLiteral should be avoided.");

    private RexLiteralConverter() {
    }

    @Override
    public Object convert(@Nonnull Date value) {
        throw NEVER_CONVERT_BACK;
    }

    @Override
    public Object convert(@Nonnull Time value) {
        throw NEVER_CONVERT_BACK;
    }

    @Override
    public Object convert(@Nonnull Timestamp value) {
        throw NEVER_CONVERT_BACK;
    }

    @Override
    public Object convert(@Nonnull byte[] value) {
        throw NEVER_CONVERT_BACK;
    }

    @Override
    public Object convert(@Nonnull Object[] value, DingoType elementType) {
        throw NEVER_CONVERT_BACK;
    }

    @Override
    public Integer convertIntegerFrom(@Nonnull Object value) {
        long v = ((BigDecimal) value).setScale(0, RoundingMode.HALF_UP).longValue();
        return NumberUtils.checkIntRange(v);
    }

    @Override
    public Long convertLongFrom(@Nonnull Object value) {
        return ((BigDecimal) value).setScale(0, RoundingMode.HALF_UP).longValue();
    }

    @Override
    public Double convertDoubleFrom(@Nonnull Object value) {
        return ((BigDecimal) value).doubleValue();
    }

    @Override
    public String convertStringFrom(@Nonnull Object value) {
        return ((NlsString) value).getValue();
    }

    @Override
    public Date convertDateFrom(@Nonnull Object value) {
        return new Date(((Calendar) value).getTimeInMillis());
    }

    @Override
    public Time convertTimeFrom(@Nonnull Object value) {
        return new Time(((Calendar) value).getTimeInMillis());
    }

    @Override
    public Timestamp convertTimestampFrom(@Nonnull Object value) {
        // This works for literal like `TIMESTAMP '1970-01-01 00:00:00'`, which returns UTC time, not local time
        Calendar calendar = (Calendar) value;
        long v = calendar.getTimeInMillis();
        return new Timestamp(v - calendar.getTimeZone().getOffset(v));
    }

    @Override
    public byte[] convertBinaryFrom(@Nonnull Object value) {
        return ((ByteString) value).getBytes();
    }
}
