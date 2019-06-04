package br.com.hubfintech.services;

import br.com.hubfintech.dto.IBaseDTO;

public abstract class BaseProcessorService<T extends IBaseDTO> {

    abstract void process(T dto);

}
