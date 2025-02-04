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

package io.dingodb.driver;

import org.apache.calcite.avatica.AvaticaPreparedStatement;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.avatica.remote.TypedValue;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DingoPreparedStatement extends AvaticaPreparedStatement {
    protected DingoPreparedStatement(
        DingoConnection connection,
        Meta.StatementHandle handle,
        Meta.Signature signature,
        int resultSetType,
        int resultSetConcurrency,
        int resultSetHoldability
    ) throws SQLException {
        super(
            connection,
            handle,
            signature,
            resultSetType,
            resultSetConcurrency,
            resultSetHoldability
        );
    }

    @Override
    protected List<TypedValue> getParameterValues() {
        return super.getParameterValues();
    }

    @Override
    public Calendar getCalendar() {
        return super.getCalendar();
    }

    // This is only used by `ServerMeta`, for local driver, the value is set when JDBC APIs are called.
    void setParameterValues(@Nonnull List<TypedValue> parameterValues) {
        for (int i = 0; i < parameterValues.size(); ++i) {
            slots[i] = parameterValues.get(i);
        }
    }

    @Override
    protected void setSignature(Meta.Signature signature) {
        super.setSignature(signature);
    }

    void clear() throws SQLException {
        if (openResultSet != null) {
            openResultSet.close();
            openResultSet = null;
        }
    }

    void createResultSet(@Nullable Meta.Frame firstFrame) throws SQLException {
        Meta.Signature signature = getSignature();
        openResultSet = ((DingoConnection) connection).newResultSet(
            this,
            signature,
            firstFrame,
            signature.sql
        );
    }
}
