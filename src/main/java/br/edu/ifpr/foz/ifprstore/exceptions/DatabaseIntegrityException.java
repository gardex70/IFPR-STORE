package br.edu.ifpr.foz.ifprstore.exceptions;

//não chechada
public class DatabaseIntegrityException extends RuntimeException {

    public DatabaseIntegrityException(String msg){
        super(msg);
    }

}
