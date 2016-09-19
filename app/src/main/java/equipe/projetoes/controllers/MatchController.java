package equipe.projetoes.controllers;

import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.models.Match;

/**
 * Created by izabella on 19/09/16.
 */
public class MatchController {

    private static MatchController sInstance;

    private List<Match> mDataset;
    private MatchController() {
        mDataset = new ArrayList<Match>();
        for (int i = 0; i < 10; i++)
        {
            mDataset.add(new Match("livro " + i, i));
        }
    }

    // singleton
    public static MatchController getInstance() {
        if (sInstance == null) {
            sInstance = new MatchController();
        }
        return sInstance;
    }

    public List<Match> getFromDB() {
        return mDataset;
    }

    public void remove(Match match) {
        mDataset.remove(match);
    }

    public void add(Match match) {
        mDataset.add(match);
    }

}
