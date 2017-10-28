import {Entity} from "./Entity.model";

export interface EntityFactory<T> {
    constructNewEntity(): Entity<T>
    constructEntity(obj: any): Entity<T>
}

export class EntityCollectionModel<T> {

    readonly entities: Entity<T>[] = [];
    readonly links: Map<String, String>;

    constructor(readonly entityName: string, body: string, entityFactory: EntityFactory<T>) {
        this.entities = body['_embedded'][entityName].map(entityFactory.constructEntity);
        // this.links = new Map<String, String>(body['_links']);
    }

}