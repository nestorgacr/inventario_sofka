package com.epa.inventario.drivenAdapters.repositorios;

import com.epa.inventario.models.mongo.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends ReactiveMongoRepository<Producto, String>
{
}
