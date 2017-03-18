
	//public float mouseX, mouseY;
		mouseLoc = new Vector2(getMouseCords().x, getMouseCords().y);

		Vector2 direction = mouseLoc.sub(player.getPos());
	
	public Vector3 getMouseCords(){
		Gdx.input.setCursorPosition((int)MathUtils.clamp(Gdx.input.getX(),0,Gdx.graphics.getWidth()),
				(int)MathUtils.clamp(Gdx.input.getY(), 0, Gdx.graphics.getHeight()));
		return cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));