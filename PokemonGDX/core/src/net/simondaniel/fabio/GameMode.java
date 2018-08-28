package net.simondaniel.fabio;

public enum GameMode {
	ONE_VS_ONE("1v1", new int[] {1,1}),
	TWO_VS_TWO("2v2", new int[] {2,2}),
	THREE_VS_THREE("3v3", new int[] {3,3}),
	NO_GAME("error", new int[] {});
	
	private final String displayName;
	private final int[] maxPlayers;
	private GameMode(String displayName, int[] maxPlayers) {
		
		this.maxPlayers = maxPlayers;
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}

	public static GameMode valueOf(int gameMode) {
		for(GameMode m : GameMode.values()) {
			if(m.ordinal() == gameMode) {
				return m;
			}
		}
		return NO_GAME;
	}
	
	public int teamCount() {
		return maxPlayers.length;
	}
	
	public int maxPlayersInTeam(int i) {
		return maxPlayers[i];
	}
}
