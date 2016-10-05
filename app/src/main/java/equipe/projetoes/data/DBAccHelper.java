package equipe.projetoes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Victor Batista on 29/04/2016.
 */

    public class DBAccHelper extends SQLiteOpenHelper {

        // Nome do banco de dados
        private static final String NOME_DO_BANCO = "cadastrolocal";
        // Versão atual do banco de dados
        private static final int VERSAO_DO_BANCO = 2;

        public DBAccHelper(Context context) {
            super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
        }

        /**
         * Cria a tabela no banco de dados, caso ela não exista.
         */
        @Override
        public final void onCreate(SQLiteDatabase db) {

            String sql = "CREATE TABLE accs ("
                    + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"
                    + ",login TEXT NOT NULL"
                    + ",email TEXT NOT NULL"
                    + ",pass TEXT NOT NULL"
                    + ",firsttime INTERGER NOT NULL"
                    + ",email_google TEXT"
                    + ",email_facebook TEXT"
                    + ");";
            db.execSQL(sql);

        }

        /**
         * Atualiza a estrutura da tabela no banco de dados, caso sua versão tenha mudado.
         */
        @Override
        public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            String sql = "DROP TABLE IF EXISTS accs ;";
            db.execSQL(sql);
            onCreate(db);
        }


}

