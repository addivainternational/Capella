
struct Requirement {
	char*   id;
	char*   text;
	double  ariScore;
    int     conjunctions;
    int     vaguePhrases;
    int     optionality;
    int     subjectivity;
    int     references;
    int     weakness;
    int     imperatives;
    int     continuances;
    int     imperatives2;
    int     references2;
};

void analyzeRequirements(struct Requirement* requirements, int numRequirements);
