package com.example.jhonatashenrique.projetotcc;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Vector;

public class PontosDAO {

    private static final String URL = "http://servidor:8080/WSTCC/services/PontosDAO?wsdl";
    private static final String NAMESPACE = "http://agoravai.tccprojeto.com.br";
    private static final String INSERIR = "inserirPontos";
    private static final String EXCLUIR = "excluirPontos";
    private static final String ATUALIZAR = "atualizarPontos";
    private static final String BUSCAR_TODOS = "buscarTodosPontos";
    private static final String BUSCAR_ID = "buscarPontosPorId";

    public boolean inserirPontos(Pontos pontos) {
        SoapObject inserirPontos = new SoapObject(NAMESPACE, INSERIR);
        SoapObject objet = new SoapObject(NAMESPACE, "pontos");
        objet.addProperty("id", pontos.getId());
        objet.addProperty("nome", pontos.getNome());
        objet.addProperty("detalhesPonto", pontos.getDetalhesPonto());
        objet.addProperty("id_linha", pontos.getId_linha());
        objet.addProperty("latitude", pontos.getLatitude());
        objet.addProperty("longitude", pontos.getLongitude());


        Log.i("teste", "Envelope: "+objet.getProperty(0) +
                " "+objet.getProperty(1) +
                " "+objet.getProperty(2) +
                " "+objet.getProperty(3) +
                " "+objet.getProperty(4) +
                " "+objet.getProperty(5));

        inserirPontos.addSoapObject(objet);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(inserirPontos);
         MarshalDouble md = new MarshalDouble();
         md.register(envelope);
        Log.i("teste", "Envelope"+envelope.env);
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

    public boolean excluirPontos(Pontos pontos) {
        SoapObject excluirPontos = new SoapObject(NAMESPACE, EXCLUIR);
        SoapObject Ponto = new SoapObject(NAMESPACE, "pontos");
        Ponto.addProperty("id", pontos.getId());
        Ponto.addProperty("id_linha", pontos.getId_linha());
        Ponto.addProperty("nome", pontos.getNome());
        Ponto.addProperty("detalhesPonto", pontos.getDetalhesPonto());
        Ponto.addProperty("latitude", pontos.getLatitude());
        Ponto.addProperty("longitude", pontos.getLongitude());

        excluirPontos.addSoapObject(Ponto);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(excluirPontos);
        MarshalDouble md = new MarshalDouble();
        md.register(envelope);
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

    public boolean excluirPontos(long id) {
        return excluirPontos(new Pontos(id, 0, "","",0,0));
    }


    public ArrayList<Pontos> buscarTodasPontos(long longer) {
        ArrayList<Pontos> lista = new ArrayList<>();
        SoapObject buscarPontos = new SoapObject(NAMESPACE, BUSCAR_TODOS);
        buscarPontos.addProperty("id", longer);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(buscarPontos);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            envelope.addMapping(NAMESPACE, "Pontos", new Pontos().getClass());
            http.call("urn: " + BUSCAR_TODOS, envelope);
            Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();
            for (SoapObject soapObject : resposta) {
                Pontos pontos = new Pontos();
                pontos.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                pontos.setId_linha(Long.parseLong(soapObject.getProperty("id_linha").toString()));
                pontos.setNome(soapObject.getProperty("nome").toString());
                pontos.setDetalhesPonto(soapObject.getProperty("detalhesPonto").toString());
                pontos.setLatitude(Double.parseDouble(soapObject.getProperty("latitude").toString()));
                pontos.setLongitude(Double.parseDouble(soapObject.getProperty("longitude").toString()));
                lista.add(pontos);
            }
        } catch( java.lang.ClassCastException e) {//trata aqui caso tenha Exception de cast quer dizer que foi so um registro
            e.printStackTrace();
            try {
                SoapObject resposta = (SoapObject) envelope.getResponse();
                if (resposta != null) {
                    Pontos pontos = new Pontos();
                    pontos.setId(Long.parseLong(resposta.getProperty("id").toString()));
                    pontos.setId_linha(Long.parseLong(resposta.getProperty("id_linha").toString()));
                    pontos.setNome(resposta.getProperty("nome").toString());
                    pontos.setDetalhesPonto(resposta.getProperty("detalhesPonto").toString());
                    pontos.setLatitude(Double.parseDouble(resposta.getProperty("latitude").toString()));
                    pontos.setLongitude(Double.parseDouble(resposta.getProperty("longitude").toString()));

                    lista.add(pontos);
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

    public Pontos buscarPontosPorId(long id) {
        Pontos pontos = null;
        SoapObject buscarPontos = new SoapObject(NAMESPACE, BUSCAR_ID);
        buscarPontos.addProperty("id", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(buscarPontos);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            http.call("urn: " + BUSCAR_ID, envelope);
            SoapObject resposta = (SoapObject) envelope.getResponse();
            pontos = new Pontos();
            pontos.setId_linha(Long.parseLong(resposta.getProperty("id_linha").toString()));
            pontos.setNome(resposta.getProperty("nome").toString());
            pontos.setDetalhesPonto(resposta.getProperty("detalhesPonto").toString());
            pontos.setLatitude(Double.parseDouble(resposta.getProperty("latitude").toString()));
            pontos.setLongitude(Double.parseDouble(resposta.getProperty("longitude").toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pontos;
    }

    public boolean atualizarPontos(Pontos pontos) {
        SoapObject atualizarPontos = new SoapObject(NAMESPACE, ATUALIZAR);
        SoapObject Ponto = new SoapObject(NAMESPACE, "pontos");

        Ponto.addProperty("id_linha", pontos.getId_linha());
        Ponto.addProperty("nome", pontos.getNome());
        Ponto.addProperty("detalhesPonto", pontos.getDetalhesPonto());
        Ponto.addProperty("latitude", pontos.getLatitude());
        Ponto.addProperty("longitude", pontos.getLongitude());
        Ponto.addProperty("id", pontos.getId());


        atualizarPontos.addSoapObject(Ponto);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        MarshalDouble md = new MarshalDouble();
        md.register(envelope);
        envelope.setOutputSoapObject(atualizarPontos);
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
