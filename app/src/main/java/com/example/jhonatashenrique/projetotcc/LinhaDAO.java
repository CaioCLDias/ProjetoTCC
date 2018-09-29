package com.example.jhonatashenrique.projetotcc;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Vector;

public class LinhaDAO {

    private static final String URL = "http://servidor:8080/WSTCC/services/LinhasDAO?wsdl";
    private static final String NAMESPACE = "http://agoravai.tccprojeto.com.br";
    private static final String INSERIR = "inserirLinhas";
    private static final String EXCLUIR = "excluirLinhas";
    private static final String ATUALIZAR = "atualizarLinhas";
    private static final String BUSCAR_TODOS = "buscarTodasLinhas";
    private static final String BUSCAR_ID = "buscarLinhasPorId";

    public boolean inserirLinha(Linha linha) {
        SoapObject inserirLinha = new SoapObject(NAMESPACE, INSERIR);
        SoapObject Linhas = new SoapObject(NAMESPACE, "linhas");
        Linhas.addProperty("id", linha.getId());
        Linhas.addProperty("nomeLinha", linha.getNomeLinha());
        Linhas.addProperty("detalhes", linha.getDetalhes());

        inserirLinha.addSoapObject(Linhas);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(inserirLinha);
       // MarshalDouble md = new MarshalDouble();
        //md.register(envelope);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            http.call("urn: " + INSERIR, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Boolean.parseBoolean(resposta.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirLinha(Linha linha) {
        SoapObject excluirLinha = new SoapObject(NAMESPACE, EXCLUIR);
        SoapObject Linhas = new SoapObject(NAMESPACE, "linhas");
        Linhas.addProperty("id", linha.getId());
        Linhas.addProperty("nomeLinha", linha.getNomeLinha());
        Linhas.addProperty("detalhes", linha.getDetalhes());

        excluirLinha.addSoapObject(Linhas);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(excluirLinha);
        //MarshalDouble md = new MarshalDouble();
        //md.register(envelope);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            http.call("urn: " + EXCLUIR, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Boolean.parseBoolean(resposta.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirLinha(long id) {
        return excluirLinha(new Linha(id, "", "",""));
    }

    public ArrayList<Linha> buscarTodasLinha() {
        ArrayList<Linha> lista = new ArrayList<>();
        SoapObject buscarLinha = new SoapObject(NAMESPACE, BUSCAR_TODOS);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(buscarLinha);
        HttpTransportSE http = new HttpTransportSE(URL, 2000);
        try {
            envelope.addMapping(NAMESPACE, "Linhas", new Linha().getClass());
            http.call("urn: " + BUSCAR_TODOS, envelope);
            Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();
            for (SoapObject soapObject : resposta) {
                Linha linha = new Linha();
                linha.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                linha.setNomeLinha(soapObject.getProperty("nomeLinha").toString());
                linha.setDetalhes(soapObject.getProperty("detalhes").toString());
                linha.setUrl(soapObject.getProperty("url").toString());
                lista.add(linha);
            }
        } catch( java.lang.ClassCastException e) {//trata aqui caso tenha Exception de cast quer dizer que foi so um registro
            e.printStackTrace();
            try {
                SoapObject resposta = (SoapObject) envelope.getResponse();
                if (resposta != null) {
                    Linha linha = new Linha();
                    linha.setId(Long.parseLong(resposta.getProperty("id").toString()));
                    linha.setNomeLinha(resposta.getProperty("nomeLinha").toString());
                    linha.setDetalhes(resposta.getProperty("detalhes").toString());
                    linha.setUrl(resposta.getProperty("url").toString());


                    lista.add(linha);
                }
                return lista;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }catch (Exception e3) {//outro tipo de Exception
            e3.printStackTrace();
            return null;
        }
        return lista;
    }

    public Linha buscarLinhaPorId(long id) {
        Linha linha = null;
        SoapObject buscarLinha = new SoapObject(NAMESPACE, BUSCAR_ID);
        buscarLinha.addProperty("id", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(buscarLinha);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            http.call("urn: " + BUSCAR_ID, envelope);
            SoapObject resposta = (SoapObject) envelope.getResponse();
            linha = new Linha();
            linha.setNomeLinha(resposta.getProperty("nomeLinha").toString());
            linha.setDetalhes(resposta.getProperty("detalhes").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return linha;
    }

    public boolean atualizarLinha(Linha linha) {
        SoapObject atualizarLinha = new SoapObject(NAMESPACE, ATUALIZAR);
        SoapObject Linhas = new SoapObject(NAMESPACE, "linhas");

        Linhas.addProperty("nomeLinha", linha.getNomeLinha());
        Linhas.addProperty("detalhes", linha.getDetalhes());
        Linhas.addProperty("id", linha.getId());


        atualizarLinha.addSoapObject(Linhas);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        //MarshalDouble md = new MarshalDouble();
        //md.register(envelope);
        envelope.setOutputSoapObject(atualizarLinha);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            http.call("urn: " + ATUALIZAR, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Boolean.parseBoolean(resposta.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
