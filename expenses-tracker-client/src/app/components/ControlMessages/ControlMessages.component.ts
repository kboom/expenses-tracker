import {Component, Input} from "@angular/core";
import {FormControl} from "@angular/forms";
import {ValidationMessageProvider} from "../../validators/validation.messages";

@Component({
    selector: 'control-messages',
    styleUrls: ['ControlMessages.scss'],
    template: `

        <div *ngIf="errorMessage !== null" class="control-messages">
            <div class="control-messages__message">{{errorMessage}}</div>
        </div>

    `
})
export class ControlMessagesComponent {

    @Input()
    control: FormControl;


    constructor(private validationMessages: ValidationMessageProvider) {
    }

    get errorMessage() {
        for (let propertyName in this.control.errors) {
            if (this.control.errors.hasOwnProperty(propertyName) && this.control.touched) {
                return this.validationMessages.getValidatorErrorMessage(propertyName, this.control.errors[propertyName]);
            }
        }
        return null;
    }

}
