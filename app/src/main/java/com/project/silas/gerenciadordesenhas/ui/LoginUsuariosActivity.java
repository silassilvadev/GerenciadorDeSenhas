package com.project.silas.gerenciadordesenhas.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.silas.gerenciadordesenhas.R;
import com.project.silas.gerenciadordesenhas.core.OperationListener;
import com.project.silas.gerenciadordesenhas.entity.Usuario;
import com.project.silas.gerenciadordesenhas.managers.LoginUsuariosManager;
import com.project.silas.gerenciadordesenhas.ui.main.TelaPrincipalActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginUsuariosActivity extends AppCompatActivity {

    @BindView(R.id.tiet_login_usuarios_email)
    protected TextInputEditText tietFormLoginEmail;

    @BindView(R.id.tiet_login_usuarios_senha)
    protected TextInputEditText tietFormLoginSenha;

    @BindView(R.id.ll_cadastrar_login_usuarios)
    protected LinearLayout llFormLoginCadastrarUsuario;

    @BindView(R.id.bt_entrar_login_usuarios)
    protected Button btFormLoginEntrar;

    @BindView(R.id.bt_sair_login_usuarios)
    protected Button btFormLoginSair;

    private LoginUsuariosManager loginUsuariosManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_usuarios_activity);
        ButterKnife.bind(this);

        this.loginUsuariosManager = new LoginUsuariosManager(this);

        btFormLoginEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tietFormLoginEmail.getText().toString().equals("") && tietFormLoginSenha.getText().toString().equals("")) {
                    Toast.makeText(LoginUsuariosActivity.this, "Preencha seus dados para entrar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUsuariosManager.efetuarLogin(new Usuario().setEmailUsuario(tietFormLoginEmail.getText().toString())
                        .setSenhaUsuario(tietFormLoginSenha.getText().toString()), new OperationListener<Usuario>(){
                    @Override
                    public void onSuccess(Usuario usuarioLogado) {
                        Toast.makeText(LoginUsuariosActivity.this, "Login efetuado corretamente!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginUsuariosActivity.this, TelaPrincipalActivity.class);
                        intent.putExtra(TelaPrincipalActivity.TAG_USUARIO_LOGADO, usuarioLogado);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable error) {
                        super.onError(error);
                        Toast.makeText(LoginUsuariosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        llFormLoginCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUsuariosActivity.this, CadastroUsuariosActivity.class));
            }
        });

        btFormLoginSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        try {

            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle(getString(R.string.st_alerta_login_usuarios))
                    .setMessage(getString(R.string.st_mensagem_sair_login_usuarios))
                    .setPositiveButton(getString(R.string.st_sim_login_usuarios), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.st_nao_login_usuarios), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create()
                    .show();

        } catch (Throwable e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }
}
