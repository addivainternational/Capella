struct Requirement {
	char* Text;
	char* Id;
	int AriScore;
};

struct Requirement* analyzeRequirements(struct Requirement* requirements, int numRequirements);
