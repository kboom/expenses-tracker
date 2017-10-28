export class TimezoneModel implements ModelEntity {

    constructor(
        public name: string,
        public locationName: string,
        public differenceToGMT: number
    ) {}

    static emptyTimezone(): TimezoneModel {
        return new TimezoneModel(null, null, null);
    }

}