package be.kuleuven.chi.backend.categories;

import java.io.Serializable;

/**
 * Created by Lies on 3/04/14.
 */
public interface Category extends Serializable {

    static final long serialVersionUID = 0L;

    public String getName();

}
