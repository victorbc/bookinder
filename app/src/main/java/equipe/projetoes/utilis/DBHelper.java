package equipe.projetoes.utilis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Victor Batista on 29/04/2016.
 */

    public class DBHelper extends SQLiteOpenHelper {

        // Nome do banco de dados
        private static final String NOME_DO_BANCO = "bibliotecalocal";
        // Versão atual do banco de dados
        private static final int VERSAO_DO_BANCO = 1;

        public DBHelper(Context context) {
            super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
        }

        /**
         * Cria a tabela no banco de dados, caso ela não exista.
         */
        @Override
        public final void onCreate(SQLiteDatabase db) {

            String sql = "CREATE TABLE livros ("
                    + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"
                    + ",name TEXT NOT NULL"
                    +  ",autor TEXT NOT NULL"
                    + ",editora TEXT NOT NULL"
                    + ",isbn TEXT NOT NULL"
                    + ",pg INTEGER NOT NULL"
                    + ",read_pg INTEGER NOT NULL"
                    + ",trade INTERGER NOT NULL"
                    + ",fav INTERGER NOT NULL"
                    + ");";
            db.execSQL(sql);

        }

        /**
         * Atualiza a estrutura da tabela no banco de dados, caso sua versão tenha mudado.
         */
        @Override
        public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            String sql = "DROP TABLE IF EXISTS livros ;";
            db.execSQL(sql);
            onCreate(db);
        }


}

