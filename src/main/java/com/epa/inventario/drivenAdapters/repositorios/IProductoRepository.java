package com.epa.inventario.drivenAdapters.repositorios;

import com.epa.inventario.models.mongo.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IProductoRepository extends ReactiveMongoRepository<Producto, String>
{
}
