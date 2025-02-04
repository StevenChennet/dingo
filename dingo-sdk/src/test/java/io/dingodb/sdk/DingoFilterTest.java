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

package io.dingodb.sdk;

import io.dingodb.common.operation.context.BasicContext;
import io.dingodb.common.operation.context.OperationContext;
import io.dingodb.common.operation.filter.DingoFilter;
import io.dingodb.common.operation.filter.impl.DingoLogicalExpressFilter;
import io.dingodb.common.operation.filter.impl.DingoValueEqualsFilter;
import io.dingodb.common.store.KeyValue;
import io.dingodb.common.table.TableDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class DingoFilterTest {

    private static TableDefinition tableDefinition;

    @BeforeAll
    public static void setupAll() throws IOException {
        tableDefinition = TableDefinition.readJson(
            DingoFilterTest.class.getResourceAsStream("/table-test.json")
        );
    }

    @Test
    public void testFilter() throws IOException {
        OperationContext context = new BasicContext().definition(tableDefinition);

        DingoFilter root = new DingoLogicalExpressFilter();
        DingoFilter equalsFilter = new DingoValueEqualsFilter(new int[]{3}, new Object[]{1});
        root.addAndFilter(equalsFilter);

        Object[] record = new Object[] {1, "a1", "a2", 1};
        KeyValue keyValue = context.keyValueCodec().encode(record);
        Assertions.assertEquals(root.filter(context, keyValue), true);

        record = new Object[] {1, "a1", "a2", 2};
        keyValue = context.keyValueCodec().encode(record);
        Assertions.assertEquals(root.filter(context, keyValue), false);

        DingoFilter equalsFilter2 = new DingoValueEqualsFilter(new int[]{1}, new Object[]{"a1"});
        root.addAndFilter(equalsFilter2);

        record = new Object[] {1, "a1", "a2", 1};
        keyValue = context.keyValueCodec().encode(record);
        Assertions.assertEquals(root.filter(context, keyValue), true);

        record = new Object[] {1, "a2", "a2", 1};
        keyValue = context.keyValueCodec().encode(record);
        Assertions.assertEquals(root.filter(context, keyValue), false);

        root = new DingoLogicalExpressFilter();
        root.addOrFilter(equalsFilter);
        root.addOrFilter(equalsFilter2);

        Assertions.assertEquals(root.filter(context, keyValue), true);

        record = new Object[] {1, "a2", "a2", 2};
        keyValue = context.keyValueCodec().encode(record);
        Assertions.assertEquals(root.filter(context, keyValue), false);
    }
}
