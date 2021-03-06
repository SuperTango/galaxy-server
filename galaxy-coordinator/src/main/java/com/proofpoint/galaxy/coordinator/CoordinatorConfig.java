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

import com.proofpoint.configuration.Config;
import com.proofpoint.units.Duration;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

public class CoordinatorConfig
{
    private String binaryRepoBase;
    private String configRepoBase;
    private Duration statusExpiration = new Duration(30, TimeUnit.SECONDS);

    @NotNull
    public String getBinaryRepoBase()
    {
        return binaryRepoBase;
    }

    @Config("coordinator.binary-repo")
    public CoordinatorConfig setBinaryRepoBase(String binaryRepoBase)
    {
        this.binaryRepoBase = binaryRepoBase;
        return this;
    }

    @NotNull
    public String getConfigRepoBase()
    {
        return configRepoBase;
    }

    @Config("coordinator.config-repo")
    public CoordinatorConfig setConfigRepoBase(String configRepoBase)
    {
        this.configRepoBase = configRepoBase;
        return this;
    }

    @NotNull
    public Duration getStatusExpiration()
    {
        return statusExpiration;
    }

    @Config("coordinator.status.expiration")
    public CoordinatorConfig setStatusExpiration(Duration statusExpiration)
    {
        this.statusExpiration = statusExpiration;
        return this;
    }
}
