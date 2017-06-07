package vd.remora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vincent-LDLC on 07/06/2017.
 */

public class OperatorsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operator, container, false);

        //Instancier vos composants graphique ici (faîtes vos findViewById)

        return view;
    }

}
