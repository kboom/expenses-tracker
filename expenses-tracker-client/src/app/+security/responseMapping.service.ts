import {Injectable} from "@angular/core";
import { Response } from "@angular/http";
import {AuthenticationModel} from "../models/Authentication.model";
import {TokenCodesModel} from "../models/Token.model";

@Injectable()
export class ResponseMappingService {

    mapIntoTokenCodes(object): TokenCodesModel {
        return new TokenCodesModel(object.token)
    }

}