package equipe.projetoes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor on 4/9/2016.
 */
public class Match implements Parcelable {

    private String bookName;
    private double bookPrice;

    public Match(String bookName, double bookPrice) {
        this.bookName = bookName;
        this.bookPrice = bookPrice;
    }

    public Match(Parcel in) {
        this.bookName = in.readString();
        this.bookPrice = in.readDouble();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public String getBookName() {
        return bookName;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookName);
        dest.writeDouble(this.bookPrice);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Match)) {
            return false;
        }
        Match other = (Match)obj;
        return bookName.equals(other.bookName);
    }
}
