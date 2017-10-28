import {Message} from "./services/eventBus.service";

export class ApplicationEvent implements Message {

    constructor(readonly type: string,
                readonly payload: any) {
    }

}

export const USER_CREATED_EVENT = "USER_ADDED_EVENT";
export const USER_REMOVED_EVENT = "USER_REMOVED_EVENT";
export const USER_CHANGED_EVENT = "USER_CHANGED_EVENT";

export const TIMEZONE_CREATED_EVENT = "USER_CHANGED_EVENT";
export const TIMEZONE_REMOVED_EVENT = "USER_CHANGED_EVENT";
export const TIMEZONE_CHANGED_EVENT = "USER_CHANGED_EVENT";