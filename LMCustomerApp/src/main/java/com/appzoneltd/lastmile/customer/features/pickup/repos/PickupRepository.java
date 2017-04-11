package com.appzoneltd.lastmile.customer.features.pickup.repos;

import android.support.annotation.NonNull;

import com.appzoneltd.lastmile.customer.R;
import com.appzoneltd.lastmile.customer.requesters.SecureUrlLocator;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.abstraction.commands.executors.CommandExecutor;
import com.base.cached.ServerMessage;
import com.base.cached.UploadImageResponse;
import com.base.presentation.repos.base.Repository;
import com.base.presentation.repos.json.JsonRequest;
import com.base.presentation.repos.json.JsonResponse;
import com.base.usecases.events.RequestMessage;
import com.base.usecases.events.ResponseMessage;
import com.base.usecases.requesters.base.EntityGateway;
import com.base.usecases.requesters.base.EntityRequester;
import com.base.usecases.requesters.server.base.HttpMethod;
import com.base.usecases.requesters.server.ssl.AuthorizedHttpsRequester;
import com.entities.cached.City;
import com.entities.cached.Country;
import com.entities.cached.PackageType;
import com.entities.cached.PickupTime;
import com.entities.cached.ShipmentService;
import com.entities.cached.ShipmentServiceTypes;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * The {@link Repository} for Pickup Feature
 * <p/>
 * Created by Ahmed Adel on 9/21/2016.
 */
@Address(R.id.addressPickupRepository)
public class PickupRepository extends Repository {

    private JsonRequest serverPostRequestCommand;
    private JsonRequest serverPutRequestCommand;

    @Override
    public void preInitialize() {

        EntityRequester requester = new AuthorizedHttpsRequester(new SecureUrlLocator());
        EntityGateway server = new EntityGateway(requester, this);
        serverPostRequestCommand = new JsonRequest(server);

        requester = new AuthorizedHttpsRequester(new SecureUrlLocator()).method(HttpMethod.PUT);
        server = new EntityGateway(requester, this);
        server.addConcurrentRequestId(R.id.requestSubmitPickupRequest);
        serverPutRequestCommand = new JsonRequest(server);

    }


    @NonNull
    @Override
    protected CommandExecutor<Long, RequestMessage> createOnRequestCommands() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.putAll(createPostRequests());
        commandExecutor.putAll(createPutRequests());
        return commandExecutor;
    }

    private CommandExecutor<Long, RequestMessage> createPostRequests() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestPickupTimeInterval, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestShipmentService, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestShipmentServiceTypes, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestPackageTypes, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestCountries, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestCreatePickupRequest, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestCities, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestCreatePackage, serverPostRequestCommand);
        commandExecutor.put((long) R.id.requestRating , serverPostRequestCommand);
        return commandExecutor;
    }

    private CommandExecutor<Long, RequestMessage> createPutRequests() {
        CommandExecutor<Long, RequestMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestSubmitPickupRequest, serverPutRequestCommand);
        return commandExecutor;
    }

    @NonNull
    @Override
    protected CommandExecutor<Long, ResponseMessage> createOnResponseCommands() {
        CommandExecutor<Long, ResponseMessage> commandExecutor = new CommandExecutor<>();
        commandExecutor.put((long) R.id.requestPickupTimeInterval, onPickupTimeIntervalResponse());
        commandExecutor.put((long) R.id.requestShipmentService, onShipmentServiceResponse());
        commandExecutor.put((long) R.id.requestShipmentServiceTypes, onShipmentServiceTypesResponse());
        commandExecutor.put((long) R.id.requestPackageTypes, onPackageTypesResponse());
        commandExecutor.put((long) R.id.requestCountries, onCountriesResponse());
        commandExecutor.put((long) R.id.requestSubmitPickupRequest, onSubmitResponse());
        commandExecutor.put((long) R.id.requestCreatePickupRequest, onCreatePickupRequest());
        commandExecutor.put((long) R.id.requestCities, onCitiesResponse());
        commandExecutor.put((long) R.id.requestCreatePackage, onCreatePackageResponse());
        commandExecutor.put((long) R.id.requestRating , onRatingResponse());
        return commandExecutor;
    }

    private JsonResponse<ServerMessage> onCreatePickupRequest() {
        return new JsonResponse<ServerMessage>(this) {
            @Override
            protected TypeToken<ServerMessage> getTypeToken() {
                return new TypeToken<ServerMessage>() {

                };
            }
        };
    }

    @NonNull
    private JsonResponse<ArrayList<Country>> onCountriesResponse() {
        return new JsonResponse<ArrayList<Country>>(this) {
            @Override
            protected TypeToken<ArrayList<Country>> getTypeToken() {
                return new TypeToken<ArrayList<Country>>() {
                };
            }
        };
    }

    @NonNull
    private JsonResponse<ArrayList<City>> onCitiesResponse() {
        return new JsonResponse<ArrayList<City>>(this) {
            @Override
            protected TypeToken<ArrayList<City>> getTypeToken() {
                return new TypeToken<ArrayList<City>>() {
                };
            }
        };
    }

    @NonNull
    private JsonResponse<ArrayList<PackageType>> onPackageTypesResponse() {
        return new JsonResponse<ArrayList<PackageType>>(this) {
            @Override
            protected TypeToken<ArrayList<PackageType>> getTypeToken() {
                return new TypeToken<ArrayList<PackageType>>() {
                };
            }
        };
    }

    @NonNull
    private JsonResponse<ArrayList<ShipmentServiceTypes>> onShipmentServiceTypesResponse() {
        return new JsonResponse<ArrayList<ShipmentServiceTypes>>(this) {
            @Override
            protected TypeToken<ArrayList<ShipmentServiceTypes>> getTypeToken() {
                return new TypeToken<ArrayList<ShipmentServiceTypes>>() {
                };
            }
        };
    }

    @NonNull
    private JsonResponse<ArrayList<ShipmentService>> onShipmentServiceResponse() {
        return new JsonResponse<ArrayList<ShipmentService>>(this) {
            @Override
            protected TypeToken<ArrayList<ShipmentService>> getTypeToken() {
                return new TypeToken<ArrayList<ShipmentService>>() {
                };
            }
        };
    }

    @NonNull
    private JsonResponse<ArrayList<PickupTime>> onPickupTimeIntervalResponse() {
        return new JsonResponse<ArrayList<PickupTime>>(this) {
            @Override
            protected TypeToken<ArrayList<PickupTime>> getTypeToken() {
                return new TypeToken<ArrayList<PickupTime>>() {
                };
            }
        };
    }

    private JsonResponse<UploadImageResponse> onSubmitResponse() {
        return new JsonResponse<UploadImageResponse>(this) {
            @Override
            protected TypeToken<UploadImageResponse> getTypeToken() {
                return new TypeToken<UploadImageResponse>() {
                };
            }
        };
    }

    private JsonResponse<ServerMessage> onCreatePackageResponse() {
        return new JsonResponse<ServerMessage>(this) {
            @Override
            protected TypeToken<ServerMessage> getTypeToken() {
                return new TypeToken<ServerMessage>() {
                };
            }
        };
    }

    private JsonResponse<ServerMessage> onRatingResponse() {
        return new JsonResponse<ServerMessage>(this) {
            @Override
            protected TypeToken<ServerMessage> getTypeToken() {
                return new TypeToken<ServerMessage>() {
                };
            }
        };
    }

    @Override
    public void onClear() {
        serverPostRequestCommand.clear();
        serverPutRequestCommand.clear();
    }
}
