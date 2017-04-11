package com.appzoneltd.lastmile.driver.services.tracking;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.annotations.interfaces.Address;
import com.base.presentation.annotations.interfaces.JsonRequest;
import com.base.presentation.annotations.interfaces.Requester;
import com.base.presentation.repos.base.Repository;
import com.base.usecases.annotations.Mock;
import com.base.usecases.requesters.base.OpenEntityGateway;
import com.base.usecases.requesters.server.websocket.AuthorizedWebSocketsRequester;
import com.base.usecases.requesters.server.websocket.SocketMessage;

@Address(R.id.addressLocationTrackingRepository)
@Requester(value = AuthorizedWebSocketsRequester.class, gateway = OpenEntityGateway.class)
class LocationTrackingRepository extends Repository {

    @Mock(R.id.requestOpenDriverLocationSocket)
    @JsonRequest(R.id.requestOpenDriverLocationSocket)
    SocketMessage requestSubmitDriverLocation;

    @Mock(R.id.requestSendDriverLocationSocketMessage)
    @JsonRequest(R.id.requestSendDriverLocationSocketMessage)
    SocketMessage requestSendDriverLocationSocketMessage;

    @Mock(R.id.requestCloseDriverLocationSocket)
    @JsonRequest(R.id.requestCloseDriverLocationSocket)
    SocketMessage requestCloseDriverLocationSocket;


}
