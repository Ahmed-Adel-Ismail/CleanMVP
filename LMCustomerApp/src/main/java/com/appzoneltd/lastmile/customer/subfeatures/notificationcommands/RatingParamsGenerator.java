package com.appzoneltd.lastmile.customer.subfeatures.notificationcommands;

import com.base.abstraction.commands.Command;
import com.entities.cached.Rating;
import com.entities.cached.RatingRequestParams;

/**
 * Created by Wafaa on 1/5/2017.
 */

public class RatingParamsGenerator implements Command<RatingRequestParams, RatingRequestParams> {

    private Rating rating;

    public RatingParamsGenerator(Rating rating) {
        this.rating = rating;
    }

    @Override
    public RatingRequestParams execute(RatingRequestParams p) {
        RatingRequestParams params = p;
        params.setDriverId(rating.getDriverId());
        params.setPackageId(rating.getPackageId());
        return params;
    }
}
