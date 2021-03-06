/**
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
package com.proofpoint.galaxy.coordinator;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.proofpoint.galaxy.shared.LifecycleState;
import com.proofpoint.galaxy.shared.SlotStatus;
import com.proofpoint.galaxy.shared.SlotStatusRepresentation;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/v1/slot/lifecycle")
public class CoordinatorLifecycleResource
{
    private final Coordinator coordinator;

    @Inject
    public CoordinatorLifecycleResource(Coordinator coordinator)
    {
        Preconditions.checkNotNull(coordinator, "coordinator must not be null");

        this.coordinator = coordinator;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response setState(String newState, @Context UriInfo uriInfo)
    {
        Preconditions.checkNotNull(newState, "newState must not be null");

        LifecycleState state;
        if ("running" .equals(newState)) {
            state = LifecycleState.RUNNING;
        }
        else if ("restarting" .equals(newState)) {
            state = LifecycleState.RUNNING;
        }
        else if ("stopped" .equals(newState)) {
            state = LifecycleState.STOPPED;
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Predicate<RemoteSlot> slotFilter = SlotFilterBuilder.build(uriInfo);
        List<SlotStatusRepresentation> representations = Lists.newArrayList();
        for (RemoteSlot remoteSlot : coordinator.getAllSlots()) {
            if (slotFilter.apply(remoteSlot)) {
                SlotStatus slotStatus;
                switch (state) {
                    case RUNNING:
                        slotStatus = remoteSlot.start();
                        break;
                    case STOPPED:
                        slotStatus = remoteSlot.stop();
                        break;
                    default:
                        throw new AssertionError();
                }

                representations.add(SlotStatusRepresentation.from(slotStatus));
            }
        }
        return Response.ok(representations).build();
     }
}
