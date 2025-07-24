let rec mcd (x,y)= match (x,y) with 
	0,_->y
	|_,0->x
	|x,y->if x>y
		then mcd (x mod y,y)
		else mcd (x, y mod x);;
	

