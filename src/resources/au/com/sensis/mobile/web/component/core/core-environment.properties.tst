########################################################
## ENVIRONMENT : TST
########################################################

# True if bundle explosion support should be enabled.
env.bundleExplosionEnabled=false

# True if bypassing of the client cache support should be enabled.
# If you enable bundle explosion, you usually want to enable this as well.
env.bypassClientCacheEnabled=false

# True if you want bundle explosion initially activated for each new session. 
# Only honoured if env.bundleExplosionEnabled=true. 
env.bundleExplosionInitialValue=false

# True if you want support for bypassing of the client cache activated for each new session. 
# Only honoured if env.bypassClientCacheEnabled=true. 
env.bypassClientCacheInitialValue=false

# Context root relative prefix for all components.
env.context.root.component.prefix=/comp/