package com.newm.util;

import android.support.v4.app.Fragment;
import com.newm.di.HasComponent;

/**
 * @author james on 8/31/17.
 */

public class BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
