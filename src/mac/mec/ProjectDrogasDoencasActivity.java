package mac.mec;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import mac.mec.R;


public class ProjectDrogasDoencasActivity extends Activity {
    /** Called when the activity is first created. */
    Button  btclean, btpesqdroga,btpesqdoenca,btA;
    TextView tvid,tvdroga,tvdoenca;
    EditText etconsulta, etresultadopesquisa,id;
    String aux;
    
    SQLiteDatabase bdsaude = null;
    Cursor cursorsaude,cursorsaudeb;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CriaAbreDataBase();
        
        /*Instancias dos objetos widgets*/
        btpesqdroga =(Button) findViewById(R.id.button1);
        btpesqdoenca =(Button) findViewById(R.id.button2); 
        btclean= (Button) findViewById(R.id.button3);//botao limpar campos 
        btA = (Button) findViewById(R.id.button5);
        
		id = (EditText) findViewById(R.id.editText1);
        etconsulta = (EditText) findViewById(R.id.editText2);
        etresultadopesquisa = (EditText) findViewById(R.id.editText3);

        /*Dar açoes aos componentes, evento de click ou toque*/
        id.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				limpacampos();
			}
		});
        
        btpesqdroga.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try{
					PesquisarBancoDroga();
					
			}catch(Exception e){
				ExibirMenssagem("Erro Pesquisa:","btdroga: "+e);
			}
		  }
		});
        
        btpesqdoenca.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				try{
					PesquisarBancoDoenca();
				}catch(Exception e){
					ExibirMenssagem("Erro Button", "btdoenca: "+e);
				}
			}
		});
        
        btclean.setOnClickListener(new View.OnClickListener() {
        	
			public void onClick(View v) {
				cursorsaude = null;
				limpacampos();
			}
		});
        
        btA.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				aux=etconsulta.getText().toString();
				etconsulta.setText("Á"+aux);
				
			}
		});
    	
    }
    
    /*Método que criar ou abre  banco de dados */
    public void CriaAbreDataBase(){
    	
    	try {
    		 String banco = "DrogasDoencasDB.sqlite";
    		 bdsaude = openOrCreateDatabase(banco, MODE_WORLD_WRITEABLE,null);
    		 String  sql= "create table if not exists drogasdoencas (_id Integer primary key autoincrement,droga text,doenca text);";
    		 String sqlb="create table if not exists doencasdrogas (_id Integer primary key autoincrement,doenca text,droga text);";
    		 bdsaude.execSQL(sql);
    		 bdsaude.execSQL(sqlb);
    		 //ExibirMenssagem("Banco de Dados:","Criado com sucesso: ) !!");
    	} catch (Exception e) {
    		
    		 ExibirMenssagem("Erro Metodo:","CriaAbreDataBase(): "+e);
    	}
     }
    
    /*Metodo Caixa de mensagem*/
	public void ExibirMenssagem(String Titulo, String Mensagem){
		AlertDialog.Builder caixaMensagem = new AlertDialog.Builder(ProjectDrogasDoencasActivity.this);
		caixaMensagem.setTitle(Titulo);
		caixaMensagem.setMessage(Mensagem);
		caixaMensagem.setNeutralButton("OK", null);
		caixaMensagem.show();
	}

	/*Metodo que realiza pesquisa no banco de dados */
	public void PesquisarBancoDroga(){
		
		try 
		{	
			String sql = " select * from drogasdoencas WHERE droga LIKE '"+etconsulta.getText().toString()+"%';";
			cursorsaude = bdsaude.rawQuery(sql,null);
			
			//ExibirMenssagem("Cursor: ", ""+cursorsaude.getCount());
			if(cursorsaude.getCount() >0){
				
				cursorsaude.moveToFirst();
				dataShowDroga();
				
			}else{
				
				ExibirMenssagem("PESQUISA :","Não há registros desta droga.");
			}
			
		} 
		catch (Exception e) 
		{
			
			ExibirMenssagem("Erro Pesquisar: ", "Banco"+e);
				
		}
				
	}
	/*Método de pesquisa ao banco de dados tabela doença*/
	public void PesquisarBancoDoenca(){
		
		try 
		{	
			String sql = " select * from doencasdrogas WHERE doenca LIKE '"+etconsulta.getText().toString()+"%';";
			cursorsaude = bdsaude.rawQuery(sql,null);
			
			//ExibirMenssagem("Cursor: ", ""+cursorsaude.getCount());
			if(cursorsaude.getCount() >0){
				
				cursorsaude.moveToFirst();
				dataShowDoenca();
				
			}else{
				
				ExibirMenssagem("PESQUISA :","Não há registros dessa doença.");
			}
			
		} 
		catch (Exception e) 
		{			
			ExibirMenssagem("Erro Pesquisar: ", "Banco"+e);		
		}
				
	}
	/*Metodo que mostras os datos retirados da tableta drogasdoencas do BD*/
	public void dataShowDroga(){
		
	    id.setText(cursorsaude.getString(cursorsaude.getColumnIndexOrThrow("_id")));
	    etconsulta.setText(cursorsaude.getString(cursorsaude.getColumnIndexOrThrow("droga")));
	    etresultadopesquisa.setText(cursorsaude.getString(cursorsaude.getColumnIndex("doenca")));
    }
	/*Metodo que mostras os datos retirados da tableta doencasdrogas do BD*/
	public void dataShowDoenca(){
		
	    id.setText(cursorsaude.getString(cursorsaude.getColumnIndexOrThrow("_id")));
	    etconsulta.setText(cursorsaude.getString(cursorsaude.getColumnIndexOrThrow("doenca")));
	    etresultadopesquisa.setText(cursorsaude.getString(cursorsaude.getColumnIndex("droga")));
    }
	/*Método que limpa os campos edittext*/  
	private void limpacampos() {
		// TODO Auto-generated method stub
		id.setText("");
		etconsulta.setText("");
		etresultadopesquisa.setText("");
	}  
}
