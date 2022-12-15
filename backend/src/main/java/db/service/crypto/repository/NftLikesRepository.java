package db.service.crypto.repository;

import db.service.crypto.model.NftLikes;
import db.service.crypto.model.NftLikesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NftLikesRepository extends JpaRepository<NftLikes,NftLikesId> {

}
