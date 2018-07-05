package com.project.silas.gerenciadordesenhas.business;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.silas.gerenciadordesenhas.core.OperationResult;
import com.project.silas.gerenciadordesenhas.entity.Site;
import com.project.silas.gerenciadordesenhas.entity.Usuario;
import com.project.silas.gerenciadordesenhas.repository.SiteDao;
import com.project.silas.gerenciadordesenhas.repository.UsuarioDao;

public class TelaPrincipalBusiness {

    private Context contexto;
    private SQLiteDatabase bancoDados;
    private UsuarioDao usuarioDao;
    private SiteDao siteDao;

    public TelaPrincipalBusiness (Context context){
        this.contexto = context;
        this.bancoDados = InicializacaoBusiness.getDatabase();
        this.usuarioDao = new UsuarioDao(this.bancoDados);
        this.siteDao = new SiteDao(this.bancoDados);
    }

    public OperationResult<Cursor> buscarLogins(Usuario usuarioLogado, String queryPesquisa) {

        OperationResult<Cursor> retornoSites = new OperationResult<>();
        String pesquisa = "%" + queryPesquisa + "%";
        Cursor cursor = null;

        try {
            cursor = this.usuarioDao.rawQuery(Query.BUSCA_SITES_USUARIO, new String[]{
                    String.valueOf(usuarioLogado.getId()), pesquisa, pesquisa
            });

            Log.i("telaPrincipalBusiness", "Qtde de registros: " + cursor.getCount());

            retornoSites.withResult(cursor);

        } catch (Throwable error){
            error.printStackTrace();
            Log.i("telaPrincipalBusiness", "Erro ao carregar sites. Mensagem: " + error.getMessage());
            retornoSites.withError(error);
        } finally {
            if (cursor != null) cursor.close();
        }
        return retornoSites;
    }

    public interface Query {
        String BUSCA_SITES_USUARIO = "SELECT " + Usuario.Metadata.TABLE_NAME + ".*"
                + ", " + Usuario.Metadata.TABLE_NAME + "." + Usuario.Metadata.FIELD_ID + " AS " + Usuario.Metadata.PK_ALIAS
                + ", " + Site.Metadata.TABLE_NAME + ".*"
                + ", " + Site.Metadata.TABLE_NAME + "." + Site.Metadata.FIELD_ID + " AS " + Site.Metadata.PK_ALIAS
                + " FROM " + Usuario.Metadata.TABLE_NAME
                + " INNER JOIN " + Site.Metadata.TABLE_ALIAS
                + " ON " + Usuario.Metadata.TABLE_NAME + "." + Usuario.Metadata.FIELD_ID + " = " + Site.Metadata.TABLE_NAME + "." + Site.Metadata.FIELD_ID_USUARIO
                + " WHERE " + Site.Metadata.TABLE_NAME + "." + Site.Metadata.FIELD_ID_USUARIO + " = ?"
                + " AND (" + Site.Metadata.TABLE_NAME + "." + Site.Metadata.FIELD_URL + " LIKE ?"
                + " OR " + Site.Metadata.TABLE_NAME + "." + Site.Metadata.FIELD_LOGIN + " LIKE ?"
                + ")"
                + " ORDER BY " + Site.Metadata.FIELD_URL + " " + Site.Metadata.ORDER_BY_DESC;
    }
}