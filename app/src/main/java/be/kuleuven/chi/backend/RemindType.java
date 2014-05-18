package be.kuleuven.chi.backend;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.kuleuven.chi.app.R;

/**
 * Created by NeleR on 17/05/2014.
 */
public enum RemindType {

    NEVER {
        @Override
        public Calendar nextRemindDate(Calendar now) {
            return now;
        }

        @Override
        public boolean hasRadioButton() {
            return false;
        }

        @Override
        public int getRadioID() {
            return 0;
        }
    },

    YEARLY {
        @Override
        public Calendar nextRemindDate(Calendar now) {
            // remind exactly one year from now
            //return new GregorianCalendar(now.YEAR + 1, now.MONTH, now.DAY_OF_MONTH, now.HOUR_OF_DAY, now.MINUTE, now.SECOND);

            // remind the first day of the next year
            return new GregorianCalendar(now.YEAR, Calendar.JANUARY, 1);
        }

        @Override
        public boolean hasRadioButton() {
            return false;
        }

        @Override
        public int getRadioID() {
            return 0;
        }
    },

    MONTHLY {
        @Override
        public Calendar nextRemindDate(Calendar now) {
            // remind exactly one month from now
            //return new GregorianCalendar(now.YEAR, now.MONTH + 1, now.DAY_OF_MONTH, now.HOUR_OF_DAY, now.MINUTE, now.SECOND);

            // remind the first day of the next month
            return new GregorianCalendar(now.YEAR, now.MONTH + 1, 1);
        }

        @Override
        public boolean hasRadioButton() {
            return true;
        }

        @Override
        public int getRadioID() {
            return R.id.radio_month;
        }
    },

    WEEKLY {
        @Override
        public Calendar nextRemindDate(Calendar now) {
            // remind exactly one week from now
            //return new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH + 7, now.HOUR_OF_DAY, now.MINUTE, now.SECOND);

            // remind the first day of the week
            Calendar newCalendar = new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH);
            newCalendar.setFirstDayOfWeek(Calendar.MONDAY);
            if(!now.after(newCalendar)) {
                newCalendar.add(Calendar.WEEK_OF_YEAR,1);
            }
            return newCalendar;
        }

        @Override
        public boolean hasRadioButton() {
            return true;
        }

        @Override
        public int getRadioID() {
            return R.id.radio_week;
        }
    },

    DAILY {
        @Override
        public Calendar nextRemindDate(Calendar now) {
            // remind exactly one day from now
            //return new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH + 1, now.HOUR_OF_DAY, now.MINUTE, now.SECOND);

            // remind the first hour of the next day
            return new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH + 1);
        }

        @Override
        public boolean hasRadioButton() {
            return true;
        }

        @Override
        public int getRadioID() {
            return R.id.radio_day;
        }
    },

    HOURLY {
        @Override
        public Calendar nextRemindDate(Calendar now) {
            // remind exactly one hour from now
            //return new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH, now.HOUR_OF_DAY + 1, now.MINUTE, now.SECOND);

            // remind the first minute of the next hour
            return new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH, now.HOUR_OF_DAY + 1, 0);
        }

        @Override
        public boolean hasRadioButton() {
            return false;
        }

        @Override
        public int getRadioID() {
            return 0;
        }
    },

    MINUTELY {
        @Override
        public Calendar nextRemindDate(Calendar now) {
            // remind exactly one minute from now
            //return new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH, now.HOUR_OF_DAY, now.MINUTE + 1, now.SECOND);

            // remind the first second of the next minute
            return new GregorianCalendar(now.YEAR, now.MONTH, now.DAY_OF_MONTH, now.HOUR_OF_DAY, now.MINUTE + 1);
        }

        @Override
        public boolean hasRadioButton() {
            return false;
        }

        @Override
        public int getRadioID() {
            return 0;
        }
    };

    public abstract Calendar nextRemindDate(Calendar now);
    public abstract boolean hasRadioButton();
    public abstract int getRadioID();
}
