export interface ModelFactory<T> {

    constructNewModel(): T
    constructModel(obj: any): T

}

export class Entity<T extends ModelEntity> {

    constructor(readonly entity: T,
                readonly links: Map<String, String>) {

    }

    static fromJSON<T>(body: any, modelFactory: ModelFactory<T>): Entity<T> {
        return new Entity(modelFactory.constructModel(body), body['_links'])
    }

    static empty<T>(entity: T): Entity<T> {
        return new Entity(entity, new Map())
    }


    withUpdatedEntity = (entity: T): Entity<T> => {
        return new Entity(entity, this.links);
    }

}