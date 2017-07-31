package com.newm.util.loader;

class Response<D> {

    private Exception mException;

    private D result;

    static <D> Response<D> ok(D data){

        Response<D> response = new Response<D>();
        response.result = data;

        return  response;
    }

    static <D> Response<D> error(Exception ex){

        Response<D> response = new Response<D>();
        response.mException = ex;

        return  response;
    }

    public boolean hasError() {

        return mException != null;
    }

    public Exception getException() {

        return mException;
    }

    public D getResult() {

        return result;
    }
}
