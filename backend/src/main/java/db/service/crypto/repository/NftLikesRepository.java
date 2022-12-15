package db.service.crypto.repository;

import db.service.crypto.model.NftEntity;
import db.service.crypto.model.NftLikes;
import db.service.crypto.model.NftLikesId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NftLikesRepository extends JpaRepository<NftLikes,NftLikesId> {
    List<NftLikes> findByPk_NftEntity(NftEntity nftEntity);
}
