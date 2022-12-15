package db.service.crypto.repository;

import db.service.crypto.model.BlockchainNetwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockchainNetworkRepository extends JpaRepository<BlockchainNetwork,String> {
    public BlockchainNetwork findByName(String name);




}
