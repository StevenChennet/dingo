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

package io.dingodb.common.codec;

import io.dingodb.common.type.TupleMapping;

import java.io.IOException;
import javax.annotation.Nonnull;

public interface Codec {
    byte[] encode(@Nonnull Object[] tuple) throws IOException, ClassCastException;

    byte[] encode(@Nonnull Object[] tuple, @Nonnull TupleMapping mapping) throws IOException, ClassCastException;

    byte[] encode(@Nonnull byte[] origin, @Nonnull Object[] tuple, @Nonnull int[] schemaIndex)
        throws IOException, ClassCastException;

    default byte[] encodeKey(@Nonnull Object[] tuple) throws IOException, ClassCastException {
        return encode(tuple);
    }

    default byte[] encodeKey(@Nonnull Object[] tuple, @Nonnull TupleMapping mapping) throws IOException, ClassCastException {
        return encode(tuple, mapping);
    }

    default byte[] encodeKey(@Nonnull byte[] origin, @Nonnull Object[] tuple, @Nonnull int[] schemaIndex)
        throws IOException, ClassCastException {
        return encode(origin, tuple, schemaIndex);
    }

    default byte[] encodeKeyForRangeScan(@Nonnull Object[] tuple) throws IOException, ClassCastException {
        return encode(tuple);
    }

    Object[] decode(@Nonnull byte[] bytes) throws IOException;

    Object[] decode(Object[] result, byte[] bytes, @Nonnull TupleMapping mapping) throws IOException;

    Object[] decode(byte[] bytes, @Nonnull int[] schemaIndex) throws IOException;

    default Object[] decodeKey(@Nonnull byte[] bytes) throws IOException {
        return decode(bytes);
    }

    default Object[] decodeKey(Object[] result, byte[] bytes, @Nonnull TupleMapping mapping) throws IOException {
        return decode(result, bytes, mapping);
    }

    default Object[] decodeKey(byte[] bytes, @Nonnull int[] schemaIndex) throws IOException {
        return decode(bytes, schemaIndex);
    }
}
