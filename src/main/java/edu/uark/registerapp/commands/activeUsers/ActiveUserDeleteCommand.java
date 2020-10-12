package edu.uark.registerapp.commands.activeUsers;

import org.springframework.stereotype.Service;
import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ActiveUserDeleteCommand implements VoidCommandInterface
{
    private String sessionKey;

    @Autowired
    private ActiveUserRepository activeUserRepository;

    public ActiveUserDeleteCommand setSessionKey(final String sessionKey) {
		this.sessionKey = sessionKey;
		return this;
	}

    public String getSessionKey()
    {
        return sessionKey;
    }

    @Transactional
    public void execute()
    {
        Optional<ActiveUserEntity> activeUserEntity = activeUserRepository.findBySessionKey(sessionKey);

        if (activeUserEntity.isPresent()) 
        {
            activeUserRepository.delete(activeUserEntity.get());
        }
    }
}
    
