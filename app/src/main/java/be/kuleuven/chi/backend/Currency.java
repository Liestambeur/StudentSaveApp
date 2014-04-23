package be.kuleuven.chi.backend;

import java.io.Serializable;

/**
 * Created by NeleR on 4/04/2014.
 */
public enum Currency implements Serializable {

    EURO {
        public String getSymbol() {
            return "€";
        }
    },

    US_DOLLAR {
        public String getSymbol() {
            return "$";
        }
    },

    UK_POUND {
        public String getSymbol() {
            return "£";
        }
    };

    public abstract String getSymbol();
}
