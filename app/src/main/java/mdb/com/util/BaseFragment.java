package mdb.com.util;

import android.app.Fragment;
import mdb.com.di.HasComponent;

/**
 * @author james on 8/31/17.
 */

public abstract class BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
