package br.com.locar.api.enums;

import br.com.locar.api.exceptions.NotFoundRegitreException;

import java.util.Optional;
import java.util.stream.Stream;

public enum EModelo {
    GOL_G7("GOL G7"),
    GOL_G8("GOL G8"),
    ONIX_LT("ONIX LT"),
    ONIX_LTZ("ONIX LTZ");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    EModelo(String descricao) {
        this.descricao = descricao;
    }

    public static EModelo getModeloByDescricao(String descricao) throws NotFoundRegitreException{
        Optional<EModelo> modeloEncontrado = Stream.of(EModelo.values()).filter(modelo -> modelo.getDescricao().equalsIgnoreCase(descricao)).findFirst();
        if(modeloEncontrado.isPresent()){
            return modeloEncontrado.get();
        }
        throw new NotFoundRegitreException("Não foi encontrado modelo configurado com a descrição: "+descricao);
    }

}
