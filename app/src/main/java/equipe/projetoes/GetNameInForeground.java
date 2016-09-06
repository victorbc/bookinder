package equipe.projetoes;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import equipe.projetoes.activities.LoginActivity;

/**
 * Created by kallynnykarlla on 29/08/16.
 */
public class GetNameInForeground extends AbstractGetNameTask {

    public GetNameInForeground(LoginActivity mActivity, String mScope, String mEmail) {
        super(mActivity, mScope, mEmail);
    }

    @Override
    protected String fetchToken() throws IOException {
        try{
            return GoogleAuthUtil.getToken(mActivity,mEmail, mScope);

        }catch (GooglePlayServicesAvailabilityException playEx){

        }catch (UserRecoverableAuthException urae){
            mActivity.startActivityForResult(urae.getIntent(),mRequest);

        }catch (GoogleAuthException fatalException){
            fatalException.printStackTrace();
        }
        return null;

    }
}
