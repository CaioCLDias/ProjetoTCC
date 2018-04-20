package com.example.jhonatashenrique.projetotcc;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by JhonatasHenrique on 18/10/2017.
 */

public class CoordDAO {
    private static final String URL = "http://192.168.0.199:8080/WSTCC/services/NserieDAO?wsdl";
    private static final String NAMESPACE = "http://agoravai.tccprojeto.com.br";
    private static final String INSERIR = "inserirNserie";
    private static final String EXCLUIR = "excluirNserie";
    private static final String ATUALIZAR = "atualizarNserie";
    private static final String BUSCAR_TODOS = "buscarTodosNserie";
    private static final String BUSCAR_ID = "buscarNseriePorId";

    public boolean inserirNserie(Coord coord) {
        SoapObject inserirNserie = new SoapObject(NAMESPACE, INSERIR);
        SoapObject Nserie = new SoapObject(NAMESPACE, "Nserie");
        Nserie.addProperty("id", coord.getId());
        Nserie.addProperty("numeroserie", coord.getCoordenadas());
        Nserie.addProperty("segundonumero", coord.getSegundo());

        inserirNserie.addSoapObject(Nserie);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(inserirNserie);
        MarshalDouble md = new MarshalDouble();
        md.register(envelope);
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

    public boolean excluirNserie(Coord coord) {
        SoapObject inserirNserie = new SoapObject(NAMESPACE, EXCLUIR);
        SoapObject Nserie = new SoapObject(NAMESPACE, "nserie");
        Nserie.addProperty("id", coord.getId());
        Nserie.addProperty("numeroserie", coord.getCoordenadas());
        Nserie.addProperty("segundo", coord.getSegundo());

        inserirNserie.addSoapObject(Nserie);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(inserirNserie);
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

    public boolean excluirNserie(int id) {
        return excluirNserie(new Coord(id, 0, 0));
    }

    public ArrayList<Coord> buscarTodosNserie() {
        ArrayList<Coord> lista = new ArrayList<Coord>();
        SoapObject buscarCoord = new SoapObject(NAMESPACE, BUSCAR_TODOS);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(buscarCoord);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            http.call("urn: " + BUSCAR_TODOS, envelope);
            Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();
            for (SoapObject soapObject : resposta) {
                Coord coord = new Coord();
                coord.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                coord.setCoordenadas(Double.parseDouble(soapObject.getProperty("numeroserie").toString()));
                coord.setSegundo(Double.parseDouble(soapObject.getProperty("segundonumero").toString()));
                lista.add(coord);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lista;
    }

    public Coord buscarNseriePorId(int id) {
        Coord coord = null;
        SoapObject buscarCoord = new SoapObject(NAMESPACE, BUSCAR_ID);
        buscarCoord.addProperty("id", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(buscarCoord);
        HttpTransportSE http = new HttpTransportSE(URL);
        try {
            http.call("urn: " + BUSCAR_ID, envelope);
            SoapObject resposta = (SoapObject) envelope.getResponse();
            coord = new Coord();
            coord.setCoordenadas(Double.parseDouble(resposta.getProperty("numeroserie").toString()));
            coord.setSegundo(Double.parseDouble(resposta.getProperty("segundonumero").toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return coord;
    }

    public boolean atualizarNserie(Coord coord) {
        SoapObject inserirNserie = new SoapObject(NAMESPACE, ATUALIZAR);
        SoapObject Nserie = new SoapObject(NAMESPACE, "nserie");
        Nserie.addProperty("numeroserie", coord.getCoordenadas());
        Nserie.addProperty("segundonumero", coord.getSegundo());
        Nserie.addProperty("id", coord.getId());

        inserirNserie.addSoapObject(Nserie);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        MarshalDouble md = new MarshalDouble();
        md.register(envelope);
        envelope.setOutputSoapObject(inserirNserie);
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
