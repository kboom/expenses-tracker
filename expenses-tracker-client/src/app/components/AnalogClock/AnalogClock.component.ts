import * as moment from "moment";
import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {extend, includes, map, pick, transform} from "lodash-es";
import {Moment} from "moment";

@Component({
    selector: 'analog-clock',
    template: `

        <div class="clocks active bounce">
            <article #clock style="width: 250px; height: 250px;" class="clock ios7 js-tokyo">
                <section class="hours-container">
                    <section #hours class="hours"></section>
                </section>
                <section class="minutes-container">
                    <section #minutes class="minutes"></section>
                </section>
                <section class="seconds-container">
                    <section #seconds class="seconds"></section>
                </section>
            </article>
        </div>

    `,
    styleUrls: ['./AnalogClock.component.scss']
})
export class AnalogClockComponent implements OnInit {

    @Input()
    dateTime: Moment;

    @ViewChild("clock")
    clock: any;

    @ViewChild("hours")
    hours: any;

    @ViewChild("minutes")
    minutes: any;

    @ViewChild("seconds")
    seconds: any;

    ngOnInit(): void {
        this.initClock();
        this.moveSecondHands();
        this.setUpMinuteHands();
    }

    initClock() {

        const hours = this.dateTime.get("hours");
        const minutes = this.dateTime.get("minutes");
        const seconds = this.dateTime.get("seconds");

        let degrees = [
            {
                hand: this.hours.nativeElement,
                degree: (hours * 30) + (minutes / 2),
                name: 'hours'
            },
            {
                hand: this.minutes.nativeElement,
                degree: (minutes * 6),
                name: 'minutes'
            },
            {
                hand: this.seconds.nativeElement,
                degree: (seconds * 6),
                name: 'seconds'
            }
        ];
        for (let j = 0; j < degrees.length; j++) {
            const degree = degrees[j];
            degree.hand['style'].webkitTransform = 'rotateZ(' + degrees[j].degree + 'deg)';
            degree.hand['style'].transform = 'rotateZ(' + degrees[j].degree + 'deg)';
            // If this is a minute hand, note the seconds position (to calculate minute position later)
            if (degree.name === 'minutes') {
                degree.hand['parentElement'].setAttribute('data-second-angle', '' + degrees[j + 1].degree);
            }
        }


        this.clock.nativeElement.className = this.clock.nativeElement.className + ' show';
    }

    /*
     * Move the second containers
     */
    moveSecondHands = () => {
        let containers = document.querySelectorAll('.bounce .seconds-container');
        setInterval(function () {
            for (let i = 0; i < containers.length; i++) {
                if (containers[i]['angle'] === undefined) {
                    containers[i]['angle'] = 6;
                } else {
                    containers[i]['angle'] += 6;
                }
                containers[i]['style'].webkitTransform = 'rotateZ(' + containers[i]['angle'] + 'deg)';
                containers[i]['style'].transform = 'rotateZ(' + containers[i]['angle'] + 'deg)';
            }
        }, 1000);
        for (let i = 0; i < containers.length; i++) {
            // Add in a little delay to make them feel more natural
            let randomOffset = Math.floor(Math.random() * (100 - 10 + 1)) + 10;
            containers[i]['style'].transitionDelay = '0.0' + randomOffset + 's';
        }
    };

    /*
     * Set a timeout for the first minute hand movement (less than 1 minute), then rotate it every minute after that
     */
    setUpMinuteHands = () => {
        // More tricky, this needs to move the minute hand when the second hand hits zero
        let containers = document.querySelectorAll('.minutes-container');
        let secondAngle = Number.parseInt(containers[containers.length - 1].getAttribute('data-second-angle'));
        if (secondAngle > 0) {
            let delay = (((360 - secondAngle) / 6) + 0.1) * 1000;
            setTimeout(() => {
                this.moveMinuteHands(containers);
            }, delay);
        }
    };

    moveMinuteHands = (containers) => {
        for (let i = 0; i < containers.length; i++) {
            containers[i].style.webkitTransform = 'rotateZ(6deg)';
            containers[i].style.transform = 'rotateZ(6deg)';
        }
        // Then continue with a 60 second interval
        setInterval(function () {
            for (let i = 0; i < containers.length; i++) {
                if (containers[i].angle === undefined) {
                    containers[i].angle = 12;
                } else {
                    containers[i].angle += 6;
                }
                containers[i].style.webkitTransform = 'rotateZ(' + containers[i].angle + 'deg)';
                containers[i].style.transform = 'rotateZ(' + containers[i].angle + 'deg)';
            }
        }, 60000);
    }

}


