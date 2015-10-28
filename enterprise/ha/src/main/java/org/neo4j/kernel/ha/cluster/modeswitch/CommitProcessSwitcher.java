/*
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.ha.cluster.modeswitch;

import org.neo4j.kernel.ha.DelegateInvocationHandler;
import org.neo4j.kernel.ha.MasterTransactionCommitProcess;
import org.neo4j.kernel.ha.SlaveTransactionCommitProcess;
import org.neo4j.kernel.ha.com.RequestContextFactory;
import org.neo4j.kernel.ha.com.master.Master;
import org.neo4j.kernel.ha.transaction.TransactionPropagator;
import org.neo4j.kernel.impl.api.TransactionCommitProcess;
import org.neo4j.kernel.impl.transaction.state.IntegrityValidator;

public class CommitProcessSwitcher extends AbstractComponentSwitcher<TransactionCommitProcess>
{
    private final MasterTransactionCommitProcess masterImpl;
    private final SlaveTransactionCommitProcess slaveImpl;

    public CommitProcessSwitcher( TransactionPropagator pusher, Master master,
            DelegateInvocationHandler<TransactionCommitProcess> delegate, RequestContextFactory requestContextFactory,
            IntegrityValidator integrityValidator, TransactionCommitProcess innerCommitProcess )
    {
        super( delegate );
        this.masterImpl = new MasterTransactionCommitProcess( innerCommitProcess, pusher, integrityValidator );
        this.slaveImpl = new SlaveTransactionCommitProcess( master, requestContextFactory );
    }

    @Override
    protected TransactionCommitProcess getSlaveImpl()
    {
        return slaveImpl;
    }

    @Override
    protected TransactionCommitProcess getMasterImpl()
    {
        return masterImpl;
    }
}
