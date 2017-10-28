import {Component, Inject} from "@angular/core";
import {MD_DIALOG_DATA} from "@angular/material";
import {extend, includes, map, pick, transform} from "lodash-es";

@Component({
    selector: 'confirmationDialog',
    template: `


        <h2 md-dialog-title>{{ details.title }}</h2>

        <md-dialog-content fxLayout='column'>
            {{ details.message }}
        </md-dialog-content>
        
        <md-dialog-actions fxLayout='row' fxLayoutAlign='space-between center'>
            <button md-button md-dialog-close>Cancel</button>
            <button md-button [md-dialog-close]="true">OK</button>
        </md-dialog-actions>

    `
})
export class ConfirmationDialogComponent {

    constructor(@Inject(MD_DIALOG_DATA) private details: any) {

    }

}
